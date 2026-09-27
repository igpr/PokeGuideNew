# PokéGuide

**VariantCode:** `POKE-POKEMON-MOD_A3_EMPTY_STATE_ACTIONS`

## Используемые эндпоинты

| Действие | URL |
|----------|-----|
| Список покемонов | `GET https://pokeapi.co/api/v2/pokemon?limit=40&offset=0` |
| Детали покемона | `GET https://pokeapi.co/api/v2/pokemon/{id}/` |

## Модификатор - Empty State Actions (MOD_A3)

Когда пользователь вводит поисковый запрос и ничего не находится,
вместо пустого экрана показывается специальное состояние с тремя действиями:

1. **Популярные** - показывает подборку известных покемонов
   (Бульбазавр, Чаризард, Бластойз, Пикачу, Вульпикс, Джигглипуф).
2. **Мне повезёт!** - открывает страницу случайного покемона.
3. **Сбросить поиск** - очищает поле поиска и возвращает полный каталог.

## Стек технологий

- Kotlin, Jetpack Compose, Coroutines
- MVVM (ViewModel → Repository → Retrofit)
- Hilt (DI)
- Retrofit + OkHttp + Gson
- Coil (загрузка картинок)
- Jetpack Navigation Compose

## Архитектура

```
com.pokeguide.app/
├── api/            
├── model/          
├── repository/     
├── di/             
├── screen/
│   ├── explore/    
│   ├── details/    
│   └── components/ 
├── navigation/     
└── theme/          
```
