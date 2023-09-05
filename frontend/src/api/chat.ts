import axios from 'axios';
import {
  EventStreamContentType,
  fetchEventSource,
} from '@microsoft/fetch-event-source';

export interface ChatRecord {
  id: number;
  name: string;
  createTime: number;
}

export interface ChatMessageRecord {
  id: number;
  content: string;
  createTime: string;
  role: string;
}

export function loadChats(applicationId: number) {
  return axios.get<ChatRecord[]>(
    `/conversation?applicationId=${applicationId}`
  );
}

export function loadMessage(id: number) {
  return axios.get<ChatMessageRecord[]>(`/conversation/${id}/messages`);
}

const prettyObject = (msg: any) => {
  const obj = msg;
  if (typeof msg !== 'string') {
    msg = JSON.stringify(msg, null, '  ');
  }
  if (msg === '{}') {
    return obj.toString();
  }
  if (msg.startsWith('```json')) {
    return msg;
  }
  return ['```json', msg, '```'].join('\n');
};

export interface ConversationOption {
  onUpdate?: (chat: ChatRecord, message: string, chunk: string) => void;
  onFinish: (message: string) => void;
  onError?: (err: Error) => void;
}

export async function conversation(
  chat: ChatRecord | null,
  applicationId: number,
  promptText: string,
  options: ConversationOption,
  controller: AbortController
) {
  let responseText = '';
  let finished = false;

  const requestTimeoutId = setTimeout(() => controller.abort(), 60000);

  const finish = () => {
    if (!finished) {
      options?.onFinish(responseText);
      finished = true;
    }
  };

  const chatPayload = {
    method: 'POST',
    body: JSON.stringify({
      prompt: promptText,
      userId: 17502168,
      conversationId: chat?.id,
      applicationId,
      stream: true,
    }),
    signal: controller.signal,
    headers: {
      'Content-Type': 'application/json',
    },
  };

  controller.signal.onabort = finish;

  await fetchEventSource('http://localhost:8080/conversation', {
    ...chatPayload,
    // eslint-disable-next-line consistent-return
    async onopen(res) {
      clearTimeout(requestTimeoutId);
      const contentType = res.headers.get('content-type');
      // eslint-disable-next-line no-console
      console.log('[OpenAI] request response content type: ', contentType);

      if (contentType?.startsWith('text/plain')) {
        responseText = await res.clone().text();
        return finish();
      }

      if (
        !res.ok ||
        !res.headers.get('content-type')?.startsWith(EventStreamContentType) ||
        res.status !== 200
      ) {
        const responseTexts = [responseText];
        let extraInfo = await res.clone().text();
        try {
          const resJson = await res.clone().json();
          extraInfo = prettyObject(resJson);
        } catch {
          /* empty */
        }

        if (res.status === 401) {
          responseTexts.push('Unauthorized');
        }

        if (extraInfo) {
          responseTexts.push(extraInfo);
        }

        responseText = responseTexts.join('\n\n');

        return finish();
      }
    },
    // eslint-disable-next-line consistent-return
    onmessage(msg) {
      // eslint-disable-next-line no-console
      if (msg.event === 'complete' || finished) {
        return finish();
      }
      const record = JSON.parse(msg.data);
      const newChat: ChatRecord = {
        createTime: record.chat.createTime,
        id: record.chat.id,
        name: record.chat.name,
      };
      const text = record.message;
      try {
        responseText += text;
        options.onUpdate?.(newChat, responseText, text);
      } catch (e) {
        // eslint-disable-next-line no-console
        console.error('[Request] parse error', text, msg);
      }
    },
    onclose() {
      finish();
    },
    onerror(e) {
      options.onError?.(e);
      throw e;
    },
    openWhenHidden: true,
  });
}
