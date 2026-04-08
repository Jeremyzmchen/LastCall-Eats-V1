package com.lastcalleats.order.service;

/**
 * 取货码服务接口。
 *
 * <p>V1：取货码的生成与核销逻辑由 {@link OrderService} 直接处理，
 * 本接口暂不实现业务方法。</p>
 *
 * <p>未来扩展方向（V2+）：
 * <ul>
 *   <li>取货码过期自动失效（定时任务）</li>
 *   <li>取货码重新发送（用户端）</li>
 *   <li>多码核销 / 批量核销（商家端）</li>
 *   <li>取货码生命周期审计日志</li>
 * </ul>
 * </p>
 */
public interface PickupCodeService {
}
