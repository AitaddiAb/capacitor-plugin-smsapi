export interface SimInfo {
  /**
   * The SIM slot number (0-based index).
   */
  slot: number;

  /**
   * The carrier name of the SIM card.
   */
  carrierName: string;

  /**
   * The display name of the SIM card.
   */
  displayName: string;

  /**
   * Whether the SIM card is currently active.
   */
  isActive: boolean;
}

export interface SmsApiPlugin {
  /**
   * Check if SMS permissions are granted.
   *
   * @since 1.0.0
   */
  checkPermissions(): Promise<{ granted: boolean; message: string }>;

  /**
   * Request SMS permissions from the user.
   *
   * @since 1.0.0
   */
  requestPermissions(): Promise<{ granted: boolean; message: string }>;

  /**
   * Send an SMS message using the specified SIM card.
   *
   * @since 1.0.0
   */
  sendSMS(options: { sim: number; recipient: string; content: string }): Promise<{ success: boolean; message: string }>;

  /**
   * Get a list of available SIM cards on the device.
   *
   * @since 1.0.0
   */
  getSIMs(): Promise<{ sims: SimInfo[] }>;
}
