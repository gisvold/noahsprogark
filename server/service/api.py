from tastypie import fields
from tastypie.resources import ModelResource
from service.models import Game, Term, Company, GameRel

class TermResource(ModelResource):

    class Meta:
        queryset = Term.objects.all();
        resource_name = 'term'
        include_resource_uri = False


class GameRelResource(ModelResource):
    term = fields.ToOneField(TermResource, 'term', full=True)
    class Meta:
        queryset = GameRel.objects.all()
        include_resource_uri = False
        excludes = ['order',]


class GameResource(ModelResource):
    terms = fields.ToManyField(GameRelResource, 'gamerel_set', related_name='term', full=True)
    class Meta:
        queryset = Game.objects.all()
        resource_name = 'game'
        include_resource_uri = False


