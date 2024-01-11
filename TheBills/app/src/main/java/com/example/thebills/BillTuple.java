package com.example.thebills;

import java.sql.Timestamp;
import java.util.UUID;

/*

teraz tak:
to jest pojedyńczy rachunek
ma swój status, który zmienia się jeżeli każdy uregulował swoją część
to czy user już uregulował pokazuje lista returnsArray
kto ile ma oddać pokazuje lista costsArray

lista ta tworzy się automatycznie, tworząć rachunek
wybieramy czy kwota rozdzielana jest ręcznie
(osoba tworząca rachunek wpisuje ile kto wisi)
albo automatycznie
podział rachunku na równe części
(właściciel rachunu też jest uwzględniany w podziale)

tworząc rachunek user:
    #1 wybiera osoby które się składały [ui]
    #2 wybiera podział (reczy/automatyczny) [ui]
    #3 tworzone są tuple (kwota(float),osoba(uuid),czyoddał(bool))
    #4 tuple są dodawane do listy

- data: Date
- owner: uuid
- totalCost: float
- status: bool
- costList: List<costTuple>
- costTuple: Tuple(cost: float; user: uuid; payed: boolean)


*/

public class BillTuple {
    private float cost;
    private UUID user;
    private boolean paid;
    private Timestamp createDate;

}
