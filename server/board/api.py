from tastypie.resources import ModelResource
from tastypie import fields

from board.models import Board

class BoardResource(ModelResource):
    game = fields.ForeignKey('game.api.GameResource', 'game')
    player = fields.ForeignKey('player.api.PlayerResource', 'player')
    class Meta:
        queryset = Board.objects.all()
        resource_name = 'board'
        include_resource_uri = False
        excludes = ['create_date',]
