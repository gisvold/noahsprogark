from tastypie import fields
from tastypie.resources import ModelResource
from company.models import Company

class CompanyResource(ModelResource):
    class Meta:
        queryset = Company.objects.all()
        resource_name = 'company'
        include_resource_uri = False
