import axios from 'axios';

export interface ApplicationRecord {
  id: number;
  name: string;
  logo: string;
  introduction: string;
}

export function queryApplicationList() {
  return axios.get('/application');
}
