from django.db import models

class Term(models.Model):
    term = models.CharField(max_length=200)

    def __unicode__(self):
        return self.term
