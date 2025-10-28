# capacitor-plugin-smsapi

Capacitor SMS API plugin

## Install

```bash
npm install capacitor-plugin-smsapi
npx cap sync
```

## API

<docgen-index>

* [`checkPermissions()`](#checkpermissions)
* [`requestPermissions()`](#requestpermissions)
* [`sendSMS(...)`](#sendsms)
* [`getSIMs()`](#getsims)
* [Interfaces](#interfaces)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### checkPermissions()

```typescript
checkPermissions() => Promise<{ granted: boolean; message: string; }>
```

Check if SMS permissions are granted.

**Returns:** <code>Promise&lt;{ granted: boolean; message: string; }&gt;</code>

**Since:** 1.0.0

--------------------


### requestPermissions()

```typescript
requestPermissions() => Promise<{ granted: boolean; message: string; }>
```

Request SMS permissions from the user.

**Returns:** <code>Promise&lt;{ granted: boolean; message: string; }&gt;</code>

**Since:** 1.0.0

--------------------


### sendSMS(...)

```typescript
sendSMS(options: { sim: number; recipient: string; content: string; }) => Promise<{ success: boolean; message: string; }>
```

Send an SMS message using the specified SIM card.

| Param         | Type                                                              |
| ------------- | ----------------------------------------------------------------- |
| **`options`** | <code>{ sim: number; recipient: string; content: string; }</code> |

**Returns:** <code>Promise&lt;{ success: boolean; message: string; }&gt;</code>

**Since:** 1.0.0

--------------------


### getSIMs()

```typescript
getSIMs() => Promise<{ sims: SimInfo[]; }>
```

Get a list of available SIM cards on the device.

**Returns:** <code>Promise&lt;{ sims: SimInfo[]; }&gt;</code>

**Since:** 1.0.0

--------------------


### Interfaces


#### SimInfo

| Prop              | Type                 | Description                               |
| ----------------- | -------------------- | ----------------------------------------- |
| **`slot`**        | <code>number</code>  | The SIM slot number (0-based index).      |
| **`carrierName`** | <code>string</code>  | The carrier name of the SIM card.         |
| **`displayName`** | <code>string</code>  | The display name of the SIM card.         |
| **`isActive`**    | <code>boolean</code> | Whether the SIM card is currently active. |

</docgen-api>
