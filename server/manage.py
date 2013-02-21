#!/usr/bin/env python
import os
import sys
import os
os.environ.setdefault('LANG','en_US')

if __name__ == "__main__":
    os.environ.setdefault("DJANGO_SETTINGS_MODULE", "pyrest.settings")

    from django.core.management import execute_from_command_line

    execute_from_command_line(sys.argv)
