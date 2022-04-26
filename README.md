# TaskDB2
OBS:In package-ul "utils" am facut o clasa cu 2 metode statice, una genereaza un jwt pe baza unui user, iar cealalta decodifi
ca jwt-ul primit ca string.

OBS: Am creeat clasa AccountService pentru a putea valida daca user-ul ce efectueaza transferul(cel ce trimite bani) este
cel logat.

OBS : Am folosit cookie-ul cu token-ul jwt pentru a gasi utilizatorul atunci cand cream un cont pentru el, dar in cazul
in care facem transfer, logica mea nu e baza pe un user si strict pe account-ul cu iBAN-ul cautat.

OBS: Am corectat signatura metodei de transfer de la tema precedenta(Am facut o clasa AccountTransfer pentru a putea
trimite un RequestBody).