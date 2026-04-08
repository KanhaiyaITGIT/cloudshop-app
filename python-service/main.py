from fastapi import FastAPI, HTTPException
from pydantic import BaseModel, EmailStr
import boto3
import os
import logging

app = FastAPI(title="ShopEasy Notification Service")
logger = logging.getLogger(__name__)


class OrderNotification(BaseModel):
    order_id: int
    user_email: EmailStr
    total_amount: float
    items_count: int


@app.get("/health")
def health():
    return {"status": "ok", "service": "notification"}


@app.post("/notify/order")
async def send_order_notification(payload: OrderNotification):
    try:
        topic_arn = os.getenv("SNS_ORDER_TOPIC_ARN", "")

        if not topic_arn:
            logger.info("SNS not configured — skipping notification")
            return {
                "success": True,
                "message": "SNS not configured, skipped",
                "order_id": payload.order_id
            }

        sns = boto3.client(
            "sns",
            region_name=os.getenv("AWS_REGION", "ap-south-1")
        )

        message = (
            f"Order Confirmed! 🛍️\n"
            f"Order ID: #{payload.order_id}\n"
            f"Items: {payload.items_count}\n"
            f"Total: Rs.{payload.total_amount:.2f}\n"
            f"Confirmation sent to: {payload.user_email}"
        )

        sns.publish(
            TopicArn=topic_arn,
            Message=message,
            Subject=f"ShopEasy Order #{payload.order_id} Confirmed"
        )

        logger.info(f"SNS notification sent for order {payload.order_id}")
        return {"success": True, "order_id": payload.order_id}

    except Exception as e:
        logger.error(f"SNS failed: {e}")
        raise HTTPException(status_code=500, detail=str(e))