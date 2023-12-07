import { WebPlugin } from '@capacitor/core';

import type { AdjustSDKPlugin } from './definitions';

export class AdjustSDKWeb extends WebPlugin implements AdjustSDKPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
