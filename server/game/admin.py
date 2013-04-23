from django.contrib import admin

from game.models import Game
from term.admin import TermsInline

class GameAdmin(admin.ModelAdmin):
    inlines = [
        TermsInline,
        ]
    exclude = ('terms','create_date',)

admin.site.register(Game, GameAdmin)
