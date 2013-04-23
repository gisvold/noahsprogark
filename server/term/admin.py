from django.contrib import admin
from game.models import Game
from term.models import Term

class TermsInline(admin.TabularInline):
    model = Game.terms.through

class TermAdmin(admin.ModelAdmin):
    inlines = [
        TermsInline,
        ]

admin.site.register(Term, TermAdmin)