from django.contrib import admin

from game.models import Game
from term.models import Term
from company.models import Company
from board.models import Board
from player.models import Player

class BoardAdmin(admin.ModelAdmin):
    pass

class PlayerAdmin(admin.ModelAdmin):
    pass

class TermsInline(admin.TabularInline):
    model = Game.terms.through


class GameAdmin(admin.ModelAdmin):
    inlines = [
        TermsInline,
        ]
    exclude = ('terms','create_date',)

class TermAdmin(admin.ModelAdmin):
    inlines = [
        TermsInline,
        ]


class CompanyAdmin(admin.ModelAdmin):
    pass

admin.site.register(Game, GameAdmin)
admin.site.register(Term, TermAdmin)
admin.site.register(Company, CompanyAdmin)
admin.site.register(Board, BoardAdmin)
admin.site.register(Player, PlayerAdmin)