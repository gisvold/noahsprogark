from tastypie.resources import ModelResource
from tastypie.constants import ALL

from player.models import Player

class PlayerResource(ModelResource):
    class Meta:
        queryset = Player.objects.all()
        resource_name = 'player'
        include_resource_uri = False
        filtering = {
            'name': ALL
        }
