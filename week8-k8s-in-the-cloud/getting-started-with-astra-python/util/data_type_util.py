from uuid import UUID
from datetime import datetime


def uuid_from_string(string):
    return UUID('{s}'.format(s=string))


def format_timestamp(string):
    if isinstance(string, str):
        return datetime.strptime(string, '%Y-%m-%dT%H:%M:%S.%fZ')

    if isinstance(string, datetime):
        return string
