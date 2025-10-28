import { registerPlugin } from '@capacitor/core';

import type { SmsApiPlugin } from './definitions';

const SmsApi = registerPlugin<SmsApiPlugin>('SmsApi', {
  web: () => import('./web').then((m) => new m.SmsApiWeb()),
});

export * from './definitions';
export { SmsApi };
