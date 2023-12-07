import { registerPlugin } from '@capacitor/core';

import type { AdjustSDKPlugin } from './definitions';

const AdjustSDK = registerPlugin<AdjustSDKPlugin>('AdjustSDK', {
  web: () => import('./web').then(m => new m.AdjustSDKWeb()),
});

export * from './definitions';
export { AdjustSDK };
