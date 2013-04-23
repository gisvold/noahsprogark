from tastypie import fields
from tastypie.resources import ModelResource
from game.models import Game
from term.api import TermResource

"""
class GameRelResource(ModelResource):
    term = fields.ToOneField(TermResource, 'term', full=True)
    class Meta:
        queryset = GameRel.objects.all()
        include_resource_uri = False
        excludes = ['order','id',]
"""

class GameResource(ModelResource):
    # terms = fields.ToManyField(GameRelResource, 'gamerel_set', related_name='term', full=True)

    class Meta:
        queryset = Game.objects.all()
        resource_name = 'game'
        include_resource_uri = False
        excludes = ['create_date',]

