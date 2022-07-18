from tkinter import CASCADE
from django.contrib.auth.models import AbstractUser
from django.db import models
from django.core.validators import MinValueValidator

CATEGORIES = (
    ("0", "Fashion"), ("1", "Electronics"), ("2", "Home"), 
    ("3", "Books"), ("4", "Toys & Games"), ("5", "Sports & Outdoors"), 
    ("6", "Health & Household"), ("7", "Other")
)

class User(AbstractUser):   # inherits from AbstractUser -> username, password, email, etc given default
    pass


class Bid(models.Model):
    amount = models.DecimalField(decimal_places=2, max_digits=10, validators=[MinValueValidator(0)])    # blank=False default
    bidder = models.ForeignKey(User, on_delete=models.CASCADE, related_name="bids")
    time = models.DateField(auto_now_add=True, blank=True)

    def __str__(self):
        return f"{self.bidder.username} bidded {self.amount} at {self.time}"

class Comment(models.Model):
    comment = models.TextField(max_length=150)
    commenter = models.ForeignKey(User, on_delete=models.CASCADE, related_name="comments")
    time = models.DateField(auto_now_add=True, blank = True)

    def __str__(self):
        return f"{self.commenter.username}: {self.comment}  -   {self.time}"

class AuctionListing(models.Model):
    item = models.CharField(max_length=64)
    description = models.CharField(max_length=250)
    price = models.DecimalField(decimal_places=2, max_digits=10, validators=[MinValueValidator(0)])
    image = models.ImageField()
    seller = models.ForeignKey(User, on_delete=models.CASCADE, related_name="selling")      # one-to-many -> ForeignKey, many-to-many -> ManyToMany
    buyer = models.ForeignKey(User, on_delete=models.CASCADE, blank=True, null=True, related_name="bought")
    created = models.DateField(auto_now_add=True)
    closed = models.BooleanField(default=False)
    category = models.CharField(max_length=64, choices=CATEGORIES, default=CATEGORIES[len(CATEGORIES) - 1][1])
    bids = models.ManyToManyField(Bid, blank=True, related_name="listing")
    comments = models.ManyToManyField(Comment, blank=True, related_name="listing")

    def __str__(self):
        if self.buyer: 
            return f"{self.seller.username} sold {self.item} to {self.buyer.username}"
        return f"{self.seller.username} listed {self.item} for ${self.price}"

class WatchList(models.Model):
    watcher = models.ForeignKey(User, on_delete=models.CASCADE, related_name="watching")
    listing = models.ForeignKey(AuctionListing, on_delete=models.CASCADE, related_name="watchlist")

    def __str__(self):
        return f"{self.watcher.username} is watching {self.listing.item} posted by {self.listing.seller.username}"

