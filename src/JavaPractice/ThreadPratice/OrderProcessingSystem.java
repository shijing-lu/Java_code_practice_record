/*
 *  Copyright (c)
 *   Author:姚宇航
 *   Time:2025-4-5 JiangsuUniversity
 */

package JavaPractice.ThreadPratice;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class OrderProcessingSystem {

    private static final Random random = new Random();

    public static void main(String[] args) {
        // 执行订单处理
        AggregateResult result = processOrder();
        System.out.println(result);
    }

    public static AggregateResult processOrder() {
        // 1. 库存服务
        CompletableFuture<Result> stockFuture = CompletableFuture.supplyAsync(() -> callStockService())
                .orTimeout(3, TimeUnit.SECONDS)
                .exceptionally(ex -> handleException("stock", ex));

        // 2. 优惠券服务（依赖库存）
        CompletableFuture<Result> couponFuture = stockFuture.thenCompose(stockResult -> {
            if (stockResult.isSuccess()) {
                return CompletableFuture.supplyAsync(() -> callCouponService())
                        .orTimeout(3, TimeUnit.SECONDS)
                        .exceptionally(ex -> handleException("coupon", ex));
            }
            return CompletableFuture.completedFuture(Result.skipped("coupon"));
        });

        // 3. 通知服务（依赖库存）
        CompletableFuture<Result> notifyFuture = stockFuture.thenCompose(stockResult -> {
            if (stockResult.isSuccess()) {
                return CompletableFuture.supplyAsync(() -> callNotifyService())
                        .orTimeout(3, TimeUnit.SECONDS)
                        .exceptionally(ex -> handleException("notify", ex));
            }
            return CompletableFuture.completedFuture(Result.skipped("notify"));
        });

        // 聚合所有结果
        CompletableFuture<AggregateResult> aggregateFuture = CompletableFuture
                .allOf(stockFuture, couponFuture, notifyFuture)
                .thenApply(v -> new AggregateResult(
                        stockFuture.join(),
                        couponFuture.join(),
                        notifyFuture.join()
                ));

        // 异常处理和回滚逻辑
        aggregateFuture.whenComplete((result, ex) -> {
            if (ex == null) {
                handleRollback(result);
                logNotificationFailure(result);
            }
        });

        return aggregateFuture.join();
    }

    private static Result handleException(String serviceName, Throwable ex) {
        if (ex instanceof TimeoutException) {
            return Result.failure(serviceName, new RuntimeException(serviceName + "服务超时"));
        }
        return Result.failure(serviceName, ex.getCause());
    }

    private static void handleRollback(AggregateResult result) {
        if (result.getStock().isSuccess() && !result.getCoupon().isSuccess()) {
            System.out.println("【回滚】库存回滚");
        }
    }

    private static void logNotificationFailure(AggregateResult result) {
        if (!result.getNotify().isSuccess()) {
            System.out.println("【日志】通知服务失败: " + result.getNotify().getMessage());
        }
    }

    private static Result callStockService() {
        try {
            Thread.sleep(2000);
            if (random.nextDouble() < 0.3) {
                throw new RuntimeException("库存不足");
            }
            return Result.success("stock");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Result.failure("stock", e);
        }
    }

    private static Result callCouponService() {
        try {
            Thread.sleep(1000);
            if (random.nextDouble() < 0.3) {
                throw new RuntimeException("优惠券核销失败");
            }
            return Result.success("coupon");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Result.failure("coupon", e);
        }
    }

    private static Result callNotifyService() {
        try {
            Thread.sleep(500);
            if (random.nextDouble() < 0.3) {
                throw new RuntimeException("短信发送失败");
            }
            return Result.success("notify");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Result.failure("notify", e);
        }
    }
}

class Result {
    private final String serviceName;
    private final boolean success;
    private final String message;

    private Result(String serviceName, boolean success, String message) {
        this.serviceName = serviceName;
        this.success = success;
        this.message = message;
    }

    public static Result success(String serviceName) {
        return new Result(serviceName, true, "成功");
    }

    public static Result failure(String serviceName, Throwable cause) {
        return new Result(serviceName, false, cause.getMessage());
    }

    public static Result skipped(String serviceName) {
        return new Result(serviceName, false, "未执行");
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("%s: %s (%s)", serviceName, success ? "成功" : "失败", message);
    }
}

class AggregateResult {
    private final Result stock;
    private final Result coupon;
    private final Result notify;

    public AggregateResult(Result stock, Result coupon, Result notify) {
        this.stock = stock;
        this.coupon = coupon;
        this.notify = notify;
    }

    public Result getStock() {
        return stock;
    }

    public Result getCoupon() {
        return coupon;
    }

    public Result getNotify() {
        return notify;
    }

    @Override
    public String toString() {
        return String.format(
                "聚合结果:\n  库存服务: %s\n  优惠券服务: %s\n  通知服务: %s",
                stock, coupon, notify
        );
    }
}
