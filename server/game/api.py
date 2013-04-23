from tastypie import fields
from tastypie.resources import ModelResource

from game.models import Game

class GameResource(ModelResource):
    company = fields.ForeignKey('company.api.CompanyResource', 'company')
    class Meta:
        queryset = Game.objects.all()
        resource_name = 'game'
        include_resource_uri = False
        excludes = ['create_date',]

