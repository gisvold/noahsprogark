from tastypie.resources import ModelResource
from tastypie import fields

from game.models import Game
from board.models import Board
from term.models import Term
# from board.models import BoardTermRel

class BoardResource(ModelResource):
    # terms = fields.ToManyField(BoardTermRel, 'boardtermrel_set', related_name='term', full=True)
    # terms = Term.objects.extra(select={'count':'SELECT COUNT(*) FROM term_term'})
    # terms = fields.ToManyField('game.api.TermResource', 'terms', related_name='t+')
    game = fields.ForeignKey('game.api.GameResource', 'game')
    player = fields.ForeignKey('player.api.PlayerResource', 'player')
    class Meta:
        queryset = Board.objects.all()
        resource_name = 'board'
        include_resource_uri = False
        excludes = ['create_date',]
