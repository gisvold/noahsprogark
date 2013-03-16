from django.contrib import admin
from service.models import Game, Term, Company

class TermsInline(admin.TabularInline):
    model = Game.terms.through


class GameAdmin(admin.ModelAdmin):
    inlines = [
        TermsInline,
        ]
    exclude = ('terms','slug','create_date',)

class TermAdmin(admin.ModelAdmin):
    inlines = [
        TermsInline,
        ]
    exclude = ('slug',)


class CompanyAdmin(admin.ModelAdmin):
    pass

admin.site.register(Game, GameAdmin)
admin.site.register(Term, TermAdmin)
admin.site.register(Company, CompanyAdmin)