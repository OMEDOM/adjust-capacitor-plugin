export declare type AdjustEnvironment = 'production' | 'sandbox';
export declare type AdjustLogLevel =
  | 'none'
  | 'error'
  | 'warning'
  | 'info'
  | 'verbose';
export declare type AdjustTrackingAuthorizationStatus = 0 | 1 | 2 | 3 | -1;
export declare type AdjustTrackingStatusResponse = {
  status: AdjustTrackingAuthorizationStatus;
};
export type AdjustInit = {
  appToken: string;
  environment?: AdjustEnvironment;
  logLevel?: AdjustLogLevel;
};

export type AdjustEvent = {
  eventToken: string;
  callbackParams?: Record<string, string>;
};

export type AdjustCallbackId = {
  eventToken: string;
  id: string;
};

export type AdjustRevenue = {
  eventToken: string;
  currency: string;
  amount: number;
  orderId?: string;
};

export type AdjustSessionOptions = {
  key: string;
  value: string;
};

export declare type AdjustAdidResponse = {
  id: string;
};
export declare type AdjustGPSAdidResponse = {
  id: string;
};
export declare type AdjustIdfaResponse = {
  id: string;
};

export interface AdjustSDKPlugin {
  ping(options: { value: string }): Promise<{ value: string }>;
  initSDK(options: AdjustInit): void;
  trackEvent(event: AdjustEvent): void;
  trackEventCallbackId(event: AdjustCallbackId): void;
  trackRevenueEvent(event: AdjustRevenue): void;
  addSessionCallbackParameter(options: AdjustSessionOptions): void;
  addSessionPartnerParameter(options: AdjustSessionOptions): void;
  getAdid(): Promise<AdjustAdidResponse>;
  //   getGPSAdid(): Promise<AdjustGPSAdidResponse>;
  showTrackingDialog(): Promise<AdjustTrackingStatusResponse>;
  getTrackingStatus(): Promise<AdjustTrackingStatusResponse>;
  //   only for iOS, android don't need, will be get null
  getIDFA(): Promise<AdjustIdfaResponse>;
}
