## Tesztfeladat:

#### Fájlfeltöltő service backend:

Az alkalmazás backend-je egy api-n keresztül kapott fájlt képes letárolni egy vagy több
fájlmegosztón.

Az alkalmazás rest végponton várja, hogy egy adott fájlt melyik fájlmegosztóra szeretnénk
feltölteni, mely egy lista lehet, tehát egy fájlt, egyszerre több helyre is feltölthetek.

Egy user-hez több fájlmegosztó beállítás is tartozhat. Tehát nekem például lehet, hogy egy
google drive és egy onedrive fiókom van, ahová szeretném a fájlokat feltölteni. De
oneDrive-ból egyszerre kettő nem lehet.

Az alkalmazásunkon keresztül lekérhetjük a feltöltött fájljaink listáját. Ezt lehessen szűrni
tárhelyre, illetve feltöltés ideje és fájlnév szerint.

Az alkalmazásnak tudnia kell felhasználókat kezelni. Minden user csak a saját beállított
tárhelyeire tud feltölteni.

A tárhelyre fájlfeltöltést nem kell megvalósítani, elég ha ilyen esetben egy előre definiálható
url-re küldjön adatokat, mint például fájlnév, userId. Ez lehet mondjuk egy webhook.site-os
url.

Próbáljuk szimulálni a különböző szolgáltatók API különbségeit.
Az egyik “külső” API x-token-t várjon, a másik pedig csak basic auth-ot a feltöltéskor.

+1: a felhasználók képesek legyenek megosztani egymással a tárhelyüket.
Ebben az esetben tudjanak feltölteni egy másik tárhelyre is, illetve listázáskor azok a fájlok is
jelenjenek meg.

+2: Fájljaink listázását oldjuk meg paginálással

A feladat megoldását git verziókezelővel vezessük.
