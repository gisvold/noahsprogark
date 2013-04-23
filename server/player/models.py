from django.db import models

class Player(models.Model):
    name = models.CharField(max_length=200)
    sessionId = models.CharField(max_length=50)

    def __unicode__(self):
        return self.name
