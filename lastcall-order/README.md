# LastCall Order Module

## TODO

---

### 1. GET /api/orders/{id}/pickup-code — 新增 qrCodeContent 字段

**说明：**
该接口除返回 6 位数字取货码 `pickupCode` 外，还需额外返回 `qrCodeContent` 字段。
`qrCodeContent` 是后端生成的结构化字符串，供前端渲染二维码图片，不应直接展示给用户。

**返回示例：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 12,
    "listingId": 21,
    "merchantId": 8,
    "productName": "Sushi Box",
    "price": 6.99,
    "status": "PAID",
    "pickupCode": "483920",
    "qrCodeContent": "{\"type\":\"pickup\",\"source\":\"lastcall-eats\",\"orderId\":12,\"userId\":3,\"merchantId\":8,\"listingId\":21,\"status\":\"PAID\"}",
    "createdAt": "2026-04-08T14:30:00"
  }
}
```

**前端使用方式：**
```jsx
// React
<QRCode value={order.qrCodeContent} />

// 或 qrcode.js
new QRCode(document.getElementById("qrcode"), {
  text: order.qrCodeContent,
  width: 200,
  height: 200
});
```

**注意事项：**
- 页面建议同时展示数字码 `pickupCode` 和二维码，方便用户扫码或手动输入
- `qrCodeContent` 由后端生成，前端不要自行拼接
- 若 `status` 不是 `PAID`，不应展示二维码

---

### 2. 身份认证 — 临时 Header 待替换为 JWT

当前为开发测试阶段，身份信息通过 Header 传入：
- 用户接口：`X-User-Id`
- 商家接口：`X-Merchant-Id`

**后续接入 auth/security 模块后，需改为从 JWT / Spring Security 的 `SecurityContext` 中获取当前登录人身份。**