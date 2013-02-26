from django.contrib.auth.models import User, Group, Permission
from api.models import Board, Game, Term
from rest_framework import serializers


class BoardSerializer(serializers.HyperlinkedModelSerializer):
	class Meta:
		model = Board

class GameSerializer(serializers.HyperlinkedModelSerializer):
	class Meta:
		model = Game

class TermSerializer(serializers.HyperlinkedModelSerializer):
	class Meta:
		model = Term

class UserSerializer(serializers.HyperlinkedModelSerializer):
	class Meta:
		model = User
		fields = ('url', 'username', 'email', 'groups')

class GroupSerializer(serializers.HyperlinkedModelSerializer):
	permissions = serializers.ManySlugRelatedField(
		slug_field='codename',
		queryset=Permission.objects.all()
	)

	class Meta:
		model = Group
		fields = ('url', 'name', 'permissions')