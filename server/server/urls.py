from django.conf.urls import patterns, include, url
# from service.api import GameResource, TermResource, BoardResource

from tastypie.api import Api

from game.api import GameResource
from term.api import TermResource
from board.api import BoardResource

# Uncomment the next two lines to enable the admin:
from django.contrib import admin
admin.autodiscover()

v1_api = Api(api_name='v1')

v1_api.register(GameResource())
v1_api.register(TermResource())
v1_api.register(BoardResource())

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'server.views.home', name='home'),
    # url(r'^server/', include('server.foo.urls')),

    url(r'^api/', include(v1_api.urls)),

    url(r'^admin/', include(admin.site.urls)),
    url(r'^admin/doc/', include('django.contrib.admindocs.urls')),
)
