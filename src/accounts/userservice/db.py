# Copyright 2019 Google LLC
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

"""
db manages interactions with the underlying database
"""

import logging
import random
from sqlalchemy import create_engine, MetaData, Table, Column, String, Date, LargeBinary
from opentelemetry.instrumentation.sqlalchemy import SQLAlchemyInstrumentor

class UserDb:
    """Provides a set of helper functions over SQLAlchemy to manage user data in the database."""

    def __init__(self, uri, logger=logging):
        """Initialize the UserDb class.

        This sets up the database connection, defines the users table schema,
        and enables tracing instrumentation for SQLAlchemy.

        Args:
            uri (str): The database connection URI.
            logger (logging.Logger, optional): A logger instance. Defaults to the root logger.
        """
        self.engine = create_engine(uri)
        self.logger = logger
        self.users_table = Table(
            'users',
            MetaData(self.engine),
            Column('accountid', String, primary_key=True),
            Column('username', String, unique=True, nullable=False),
            Column('passhash', LargeBinary, nullable=False),
            Column('firstname', String, nullable=False),
            Column('lastname', String, nullable=False),
            Column('birthday', Date, nullable=False),
            Column('timezone', String, nullable=False),
            Column('address', String, nullable=False),
            Column('state', String, nullable=False),
            Column('zip', String, nullable=False),
            Column('ssn', String, nullable=False),
        )

        # Set up tracing autoinstrumentation for sqlalchemy
        SQLAlchemyInstrumentor().instrument(
            engine=self.engine,
            service='users',
        )

    def add_user(self, user):
        """Add a user to the database.

        Args:
            user (dict): A dictionary containing the user's data, including:
                         'username', 'passhash', 'firstname', 'lastname', 'birthday',
                         'timezone', 'address', 'state', 'zip', and 'ssn'.

        Raises:
            SQLAlchemyError: If there is an issue executing the database query.
        """
        statement = self.users_table.insert().values(user)
        self.logger.debug('QUERY: %s', str(statement))
        with self.engine.connect() as conn:
            conn.execute(statement)

    def generate_accountid(self):
        """Generate a unique alphanumeric account ID.

        This function generates a random 10-digit account ID and ensures its uniqueness
        by querying the database.

        Returns:
            str: A unique 10-digit alphanumeric account ID.
        """
        self.logger.debug('Generating an account ID')
        accountid = None
        with self.engine.connect() as conn:
            while accountid is None:
                accountid = str(random.randint(1_000_000_000, (10_000_000_000 - 1)))

                statement = self.users_table.select().where(
                    self.users_table.c.accountid == accountid
                )
                self.logger.debug('QUERY: %s', str(statement))
                result = conn.execute(statement).first()
                # If there already exists an account, try again.
                if result is not None:
                    accountid = None
                    self.logger.debug('RESULT: account ID already exists. Trying again')
        self.logger.debug('RESULT: account ID generated.')
        return accountid

    def get_user(self, username):
        """Retrieve user data from the database by username.

        Args:
            username (str): The username of the user to retrieve.

        Returns:
            dict: A dictionary containing the user's data if found, including:
                  'accountid', 'username', 'passhash', 'firstname', 'lastname',
                  'birthday', 'timezone', 'address', 'state', 'zip', and 'ssn'.
                  Returns None if no user is found with the given username.

        Raises:
            SQLAlchemyError: If there is an issue executing the database query.
        """
        statement = self.users_table.select().where(self.users_table.c.username == username)
        self.logger.debug('QUERY: %s', str(statement))
        with self.engine.connect() as conn:
            result = conn.execute(statement).first()
        self.logger.debug('RESULT: fetched user data for %s', username)
        return dict(result) if result is not None else None
