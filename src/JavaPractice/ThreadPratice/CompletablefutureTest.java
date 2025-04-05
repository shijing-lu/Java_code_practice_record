/*
 *  Copyright (c)
 *   Author:姚宇航
 *   Time:2025-4-4 JiangsuUniversity
 */

package JavaPractice.ThreadPratice;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/*
### 题目：电商订单异步处理系统
    需求背景
用户在下单后，系统需要完成三个核心异步操作（假设每个操作调用外部服务，都有网络延迟）：
1. 库存服务：扣减商品库存（耗时2秒，可能失败）
2. 优惠券服务：核销用户使用的优惠券（耗时1秒，可能失败）
3. 通知服务：发送下单成功短信给用户（耗时0.5秒）
需要实现的功能：
1. 并行优化：要求尽可能并行执行这三个操作，但优惠券核销需要依赖扣减库存成功后才能执行（业务规则：必须先确保库存足够）
2. 结果聚合：在所有操作完成后（无论成功或失败），需要返回一个包含三个操作状态的结果对象
3. 异常处理：
    - 若任意一个服务失败（比如库存不足），需要取消尚未执行的任务（如已扣减库存但优惠券核销失败时，需要回滚库存）
    - 如果通知服务失败，仅需记录日志，不影响主流程
4. 超时控制：单个服务最长等待3秒，超时按失败处理

技术要求：
- 使用`CompletableFuture`实现异步组合（至少覆盖：`thenCompose`、`thenApply`、`thenCombine`、`allOf`、`exceptionally`）
- 用随机失败模拟真实网络异常（比如随机抛出一个`RuntimeException`）
- 提供模拟的回滚逻辑方法（仅需打印日志）
 * */

public class CompletablefutureTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        CompletableFuture<String> inventoryServer = CompletableFuture.supplyAsync(() -> {
            System.out.println("开始扣减库存");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "扣减库存成功";
        });

        CompletableFuture<String> couponServer = CompletableFuture.supplyAsync(() -> {
            System.out.println("开始核销优惠券");
            try {
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "核销优惠券成功";
        });

        CompletableFuture<String> notifyServer = CompletableFuture.supplyAsync(() -> {
            System.out.println("开始发送短信");
            try {
                Thread.sleep(500);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "发送短信成功";
        });

        Thread.sleep(3000);
    }
}