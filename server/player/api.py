from tastypie.resources import ModelResource
from tastypie.authorization import Authorization
from tastypie.constants import ALL

from player.models import Player

class PlayerResource(ModelResource):
    class Meta:
        queryset = Player.objects.all()
        resource_name = 'player'
        authorization = Authorization()
        filtering = {
            'name': ALL
        }
        always_return_data=True
