from django.contrib import admin
from board.models import Board

class BoardAdmin(admin.ModelAdmin):
    pass

admin.site.register(Board, BoardAdmin)