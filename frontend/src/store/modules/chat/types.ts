export interface ChatMessage {
  createTime: string;
  content: string;
  inversion?: boolean;
  error?: boolean;
  loading?: boolean;
  localId: number;
}

export interface ChatMapping {
  [key: number]: any;
}

export interface ChatMessageMapping {
  [key: number]: any;
}

export interface Chat {
  id: number | null;
  localId: number;
  name: string;
  messages: ChatMessage[];
  mapping: ChatMessageMapping;
}

export interface ChatState {
  active: Chat | null;
  chats: Chat[];
  mapping: ChatMapping;
}
