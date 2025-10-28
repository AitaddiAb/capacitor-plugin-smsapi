import { WebPlugin } from '@capacitor/core';

import type { SmsApiPlugin, SimInfo } from './definitions';

export class SmsApiWeb extends WebPlugin implements SmsApiPlugin {
  async checkPermissions(): Promise<{ granted: boolean; message: string }> {
    console.info('Checking SMS permission on web platform');
    return { granted: false, message: 'SMS permission not available on web platform' };
  }

  async requestPermissions(): Promise<{ granted: boolean; message: string }> {
    console.info('Requesting SMS permission on web platform');
    return { granted: false, message: 'SMS permission request not available on web platform' };
  }

  async sendSMS(options: {
    sim: number;
    recipient: string;
    content: string;
  }): Promise<{ success: boolean; message: string }> {
    console.info('Sending SMS on web platform', options);
    return { success: false, message: 'SMS send not available on web platform' };
  }

  async getSIMs(): Promise<{ sims: SimInfo[] }> {
    console.info('Getting SIM information on web platform');
    return { sims: [] };
  }
}
