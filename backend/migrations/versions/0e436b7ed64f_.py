"""empty message

Revision ID: 0e436b7ed64f
Revises: 03f7d60c3d2f
Create Date: 2023-12-12 11:26:54.721020

"""
from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision = '0e436b7ed64f'
down_revision = '03f7d60c3d2f'
branch_labels = None
depends_on = None


def upgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    with op.batch_alter_table('persons', schema=None) as batch_op:
        batch_op.alter_column('phone_number',
               existing_type=sa.VARCHAR(length=15),
               type_=sa.String(length=20),
               existing_nullable=True)

    # ### end Alembic commands ###


def downgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    with op.batch_alter_table('persons', schema=None) as batch_op:
        batch_op.alter_column('phone_number',
               existing_type=sa.String(length=20),
               type_=sa.VARCHAR(length=15),
               existing_nullable=True)

    # ### end Alembic commands ###
