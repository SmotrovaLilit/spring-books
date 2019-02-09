## Add book
```
book-create --book "Руслан и людмила|2008|Хорошая книга|Пушкин А.С."
```

## Update book
```
book-update --id "5c5f49a5d46c0033314fa845" --book "Руслан и людмила|2009|Хорошая книга|другой А.С."
```

## Delete book
```
book-delete "5c5f49a5d46c0033314fa845"
```

## List book
```
book-list
```

## Add comment
book-add-comment --id "5c5f4d0dd46c0033cee60d05" --message "some message"
```

## List comments
book-comments --id "5c5f4d0dd46c0033cee60d05"
