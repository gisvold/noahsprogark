from tastypie import fields
from tastypie.resources import ModelResource
from tastypie.authorization import Authorization

from game.models import Game

class GameResource(ModelResource):
    company = fields.ForeignKey('company.api.CompanyResource', 'company')
    golden_word = fields.ForeignKey('term.api.TermResource', 'golden_word', full=True)
    bingo_leader = fields.ForeignKey('player.api.PlayerResource', 'bingo_leader', full=True, null=True)

    class Meta:
        queryset = Game.objects.all()
        resource_name = 'game'
        authorization = Authorization()
        ordering = [ 'id' ]
