from django.conf.urls import patterns, url, include
from rest_framework.urlpatterns import format_suffix_patterns
from api.views import UserList, UserDetail, GroupList, GroupDetail, BoardList, BoardDetail, GameList, GameDetail, TermList, TermDetail

urlpatterns = patterns('api.views',
    url(r'^$', 'api_root'),
    url(r'^users/$', UserList.as_view(), name='user-list'),
    url(r'^users/(?P<pk>\d+)/$', UserDetail.as_view(), name='user-detail'),
    url(r'^groups/$', GroupList.as_view(), name='group-list'),
    url(r'^groups/(?P<pk>\d+)/$', GroupDetail.as_view(), name='group-detail'),
    url(r'^boards/$', BoardList.as_view(), name='board-list'),
    url(r'^boards/(?P<pk>\d+)/$', BoardDetail.as_view(), name='board-detail'),
    url(r'^games/$', GameList.as_view(), name='game-list'),
    url(r'^games/(?P<pk>\d+)/$', GameDetail.as_view(), name='game-detail'),
    url(r'^terms/$', TermList.as_view(), name='term-list'),
    url(r'^terms/(?P<pk>\d+)/$', TermDetail.as_view(), name='term-detail'),
)

# Format suffixes
urlpatterns = format_suffix_patterns(urlpatterns, allowed=['json', 'api'])

# Default login/logout views
urlpatterns += patterns('',
    url(r'^api-auth/', include('rest_framework.urls', namespace='rest_framework'))
)