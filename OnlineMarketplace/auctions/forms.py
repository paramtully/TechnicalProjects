from django.forms import ModelForm
from django import forms

from .models import *

class CreateForm(ModelForm):
    class Meta:
        model = AuctionListing
        fields = ["item", "description", "price", "image", "category"]
        widgets = {
            "item": forms.TextInput(attrs={
                "placeholder": "Item"
                }),
            "description": forms.TextInput(attrs={
                "placeholder": "Description"
                }),
            "price": forms.TextInput(attrs={
                "placeholder": "Price"
                })
        }
        labels = {
            "item": "",
            "description": "",
            "price": ""
        }

class BidForm(ModelForm):
    class Meta:
        model = Bid
        fields = ["amount"]
        widgets = {
            "amount": forms.TextInput(attrs={
                "placeholder": "Bid"
                })
        }
        labels = {
            "amount": ""
        }

class CommentForm(ModelForm):
    class Meta:
        model = Comment
        fields = ["comment"]
        widgets = {
            "comment": forms.TextInput(attrs={
                "placeholder": "Comment"
                })
        }
        labels = {
            "comment": ""
        }