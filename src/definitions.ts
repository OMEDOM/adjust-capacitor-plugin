export interface AdjustSDKPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
