from django.conf.urls import patterns, include, url
from service.api import GameResource, TermResource, BoardResource

# Uncomment the next two lines to enable the admin:
from django.contrib import admin
admin.autodiscover()

game_resource = GameResource()
term_resource = TermResource()
board_resource = BoardResource()

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'server.views.home', name='home'),
    # url(r'^server/', include('server.foo.urls')),

    (r'^api/', include(game_resource.urls)),
    (r'^api/', include(term_resource.urls)),
    (r'^api/', include(board_resource.urls)),

    url(r'^admin/', include(admin.site.urls)),
    url(r'^admin/doc/', include('django.contrib.admindocs.urls')),
)
