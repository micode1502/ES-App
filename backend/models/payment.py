from app import db
from sqlalchemy import Column, Integer, Double, ForeignKey, DateTime, func


class Payment(db.Model):
    __tablename__ = "payments"

    id = Column(Integer, autoincrement=True, primary_key=True)
    invoice_id = Column(Integer, ForeignKey("invoices.id"), nullable=False)
    method = Column(Integer, nullable=False)
    amount = Column(Double, nullable=False)
    created_at = Column(DateTime, default=func.current_timestamp())
    updated_at = Column(
        DateTime,
        default=func.current_timestamp(),
        onupdate=func.current_timestamp(),
    )

    def __init__(self, invoice_id, method, amount):
        self.invoice_id = invoice_id
        self.method = method
        self.amount = amount

    def __repr__(self):
        return f"<Payment {self.id}>"
