from django.contrib import admin

class BoardAdmin(admin.ModelAdmin):
    pass

admin.site.register(Board, BoardAdmin)