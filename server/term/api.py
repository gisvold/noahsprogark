from tastypie.resources import ModelResource

from term.models import Term

class TermResource(ModelResource):
    class Meta:
        queryset = Term.objects.all()
        resource_name = 'term'
        include_resource_uri = False