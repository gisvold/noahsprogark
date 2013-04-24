from tastypie.resources import ModelResource
from tastypie import fields
from tastypie.authorization import Authorization
from tastypie.constants import ALL_WITH_RELATIONS, ALL

from board.models import Board

class BoardResource(ModelResource):
    game = fields.ForeignKey('game.api.GameResource', 'game', full=True)
    player = fields.ForeignKey('player.api.PlayerResource', 'player')
    class Meta:
        queryset = Board.objects.all()
        resource_name = 'board'
        authorization = Authorization()
        filtering = {
            'player': ALL_WITH_RELATIONS,
            'active': ALL
        }
        always_return_data=True
