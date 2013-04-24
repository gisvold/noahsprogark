from tastypie.resources import ModelResource
from tastypie import fields
from tastypie.authorization import Authorization

from board.models import Board

class BoardResource(ModelResource):
    game = fields.ForeignKey('game.api.GameResource', 'game', full=True)
    player = fields.ForeignKey('player.api.PlayerResource', 'player', full=True)
    class Meta:
        queryset = Board.objects.all()
        resource_name = 'board'
        authorization = Authorization()
        excludes = ['create_date',]
        always_return_data=True
