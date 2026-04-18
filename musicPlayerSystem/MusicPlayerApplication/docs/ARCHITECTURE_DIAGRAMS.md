# Music Player System - UML Class Diagram

## Complete UML Class Diagram

```mermaid
classDiagram
    class MusicPlayerApplication{
        -instance: MusicPlayerApplication
        -songLibrary: List~Song~
        -MusicPlayerApplication()
        +getInstance(): MusicPlayerApplication
        +createSongInLibrary(title, artist, filePath): void
        +getSongByTitle(title): Song
        +createPlaylist(playlistName): void
        +addSongToPlaylist(playlistName, songTitle): void
        +connectToAudioDevice(deviceType): void
        +selectPlayStrategy(type): void
        +loadPlaylist(playlistName): void
        +playSingleSong(songName): void
        +pauseCurrentSong(songName): void
        +playAllTracksInPlaylist(): void
        +playPreviousTrackInPlaylist(): void
        +QueuesNextSong(songName): void
    }
    
    class MusicPlayerFacade{
        -instance: MusicPlayerFacade
        -audioEngine: AudioEngine
        -loadedPlaylist: Playlist
        -playStrategy: IPlayStrategy
        -MusicPlayerFacade()
        +getInstance(): MusicPlayerFacade
        +connectDevice(deviceType): void
        +setPlayStrategy(strategyType): void
        +loadPlaylist(name): void
        +play(song): void
        +pause(song): void
        +playAllTracks(): void
        +playPreviousTrack(): void
        +playNextTrack(): void
    }
    
    class AudioEngine{
        -currentSong: Song
        -isPaused: boolean
        +loadSong(song): void
        +play(device, song): void
        +pause(): void
        +getCurrentSongTitle(): String
        +isPaused(): boolean
    }
    
    class DeviceManager{
        -instance: DeviceManager
        -currentOutputDevice: IAudioOutputDevice
        -DeviceManager()
        +getInstance(): DeviceManager
        +connect(deviceType): void
        +getConnectedOutputDevice(): IAudioOutputDevice
        +hasConnectedDevice(): boolean
    }
    
    class PlaylistManager{
        -instance: PlaylistManager
        -playlistMap: Map~String, Playlist~
        -PlaylistManager()
        +getInstance(): PlaylistManager
        +createPlaylist(name): void
        +getPlaylistByName(name): Playlist
        +addSongToPlaylist(playlistName, song): void
    }
    
    class StrategyManager{
        -instance: StrategyManager
        -StrategyManager()
        +getInstance(): StrategyManager
        +getCurrentStrategy(type): IPlayStrategy
    }
    
    class IPlayStrategy{
        <<interface>>
        +setPlaylist(playlist): void
        +hasNext(): boolean
        +next(): Song
        +hasPrevious(): boolean
        +previous(): Song
        +addToNext(song)*: void
    }
    
    class SequentialPlayStrategy{
        -playlist: Playlist
        -currentIndex: int
        +SequentialPlayStrategy()
        +setPlaylist(playlist): void
        +hasNext(): boolean
        +next(): Song
        +hasPrevious(): boolean
        +previous(): Song
    }
    
    class RandomPlayStrategy{
        -playlist: Playlist
        -random: Random
        -usedIndices: Set~Integer~
        +RandomPlayStrategy()
        +setPlaylist(playlist): void
        +hasNext(): boolean
        +next(): Song
        +hasPrevious(): boolean
        +previous(): Song
    }
    
    class CustomQueueStrategy{
        -customQueue: Queue~Song~
        -playlist: Playlist
        +CustomQueueStrategy()
        +setPlaylist(playlist): void
        +hasNext(): boolean
        +next(): Song
        +hasPrevious(): boolean
        +previous(): Song
        +addToNext(song): void
    }
    
    class IAudioOutputDevice{
        <<interface>>
        +playAudio(song): void
    }
    
    class BluetoothSpeakerAdaptor{
        -bluetoothApi: BluetoothSpeakerApi
        +BluetoothSpeakerAdaptor()
        +playAudio(song): void
    }
    
    class WiredSpeakerAdaptor{
        -wiredApi: WiredSpeakerAPI
        +WiredSpeakerAdaptor()
        +playAudio(song): void
    }
    
    class HeadphonesAdaptor{
        -headphonesApi: HeadphonesAPI
        +HeadphonesAdaptor()
        +playAudio(song): void
    }
    
    class BluetoothSpeakerApi{
        <<external>>
        +playOnBluetooth(songPath): void
    }
    
    class WiredSpeakerAPI{
        <<external>>
        +playOnWiredSpeaker(songPath): void
    }
    
    class HeadphonesAPI{
        <<external>>
        +playOnHeadphones(songPath): void
    }
    
    class DeviceFactory{
        +createDevice(deviceType): IAudioOutputDevice$
    }
    
    class StrategyFactory{
        +createStrategy(strategyType): IPlayStrategy$
    }
    
    class Playlist{
        -name: String
        -songList: List~Song~
        +Playlist(name)
        +getName(): String
        +getSongs(): List~Song~
        +getSize(): int
        +addSongToPlaylist(song): void
    }
    
    class Song{
        -title: String
        -artist: String
        -filePath: String
        +Song(title, artist, filePath)
        +getTitle(): String
        +getArtist(): String
        +getFilePath(): String
    }
    
    class DeviceType{
        <<enum>>
        BLUETOOTH
        WIRED
        HEADPHONES
    }
    
    class PlayStrategyType{
        <<enum>>
        SEQUENTIAL
        RANDOM
        CUSTOM_QUEUE
    }
    
    %% Relationships
    MusicPlayerApplication --> MusicPlayerFacade
    MusicPlayerFacade --> AudioEngine
    MusicPlayerFacade --> DeviceManager
    MusicPlayerFacade --> PlaylistManager
    MusicPlayerFacade --> StrategyManager
    MusicPlayerFacade --> IPlayStrategy
    
    DeviceManager --> IAudioOutputDevice
    AudioEngine --> IAudioOutputDevice
    AudioEngine --> Song
    
    IAudioOutputDevice <|.. BluetoothSpeakerAdaptor
    IAudioOutputDevice <|.. WiredSpeakerAdaptor
    IAudioOutputDevice <|.. HeadphonesAdaptor
    
    BluetoothSpeakerAdaptor --> BluetoothSpeakerApi
    WiredSpeakerAdaptor --> WiredSpeakerAPI
    HeadphonesAdaptor --> HeadphonesAPI
    
    IPlayStrategy <|.. SequentialPlayStrategy
    IPlayStrategy <|.. RandomPlayStrategy
    IPlayStrategy <|.. CustomQueueStrategy
    
    IPlayStrategy --> Playlist
    Playlist --> Song
    PlaylistManager --> Playlist
    StrategyManager --> IPlayStrategy
    DeviceFactory --> IAudioOutputDevice
    StrategyFactory --> IPlayStrategy
```

## Simplified Component Diagram

```mermaid
graph TD
    User["👤 User"]
    App[MusicPlayerApplication<br/>Singleton]
    Facade[MusicPlayerFacade<br/>Facade + Singleton]
    Engine[AudioEngine<br/>Core]
    
    DM[DeviceManager<br/>Singleton]
    PM[PlaylistManager<br/>Singleton]
    SM[StrategyManager<br/>Singleton]
    
    Devices["Audio Devices<br/>Bluetooth/Wired/Headphones"]
    Strategies["Play Strategies<br/>Sequential/Random/Custom"]
    
    User -->|Uses| App
    App -->|Delegates| Facade
    Facade -->|Manages| Engine
    Facade -->|Uses| DM
    Facade -->|Uses| PM
    Facade -->|Uses| SM
    
    DM -->|Controls| Devices
    Engine -->|Plays via| Devices
    SM -->|Provides| Strategies
    Facade -->|Uses| Strategies
```

## Pattern Application Diagram

```mermaid
graph LR
    subgraph Patterns["Design Patterns"]
        Singleton["🔒 Singleton<br/>MusicPlayerApplication<br/>MusicPlayerFacade<br/>DeviceManager<br/>PlaylistManager<br/>StrategyManager"]
        
        Strategy["📊 Strategy<br/>IPlayStrategy<br/>Sequential<br/>Random<br/>CustomQueue"]
        
        Adapter["🔌 Adapter<br/>BluetoothSpeakerAdaptor<br/>WiredSpeakerAdaptor<br/>HeadphonesAdaptor"]
        
        Facade["🎭 Facade<br/>MusicPlayerFacade<br/>Simplifies Complex<br/>Subsystems"]
        
        Factory["🏭 Factory<br/>DeviceFactory<br/>StrategyFactory"]
    end
    
    subgraph Benefits["Benefits"]
        B1["Single Instance<br/>Thread Safety"]
        B2["Algorithm<br/>Flexibility"]
        B3["API<br/>Integration"]
        B4["Simplified<br/>Interface"]
        B5["Decoupled<br/>Creation"]
    end
    
    Singleton --> B1
    Strategy --> B2
    Adapter --> B3
    Facade --> B4
    Factory --> B5
```

## Interaction Flow Diagram

```mermaid
sequenceDiagram
    participant User
    participant MPA as MusicPlayerApp
    participant MPF as MusicPlayerFacade
    participant AE as AudioEngine
    participant DM as DeviceManager
    participant SM as StrategyManager
    participant PM as PlaylistManager
    participant IOD as IAudioOutputDevice
    
    User->>MPA: Initialize App
    activate MPA
    MPA->>MPF: getInstance()
    activate MPF
    MPF->>AE: Initialize Engine
    MPF->>DM: getInstance()
    MPF->>PM: getInstance()
    MPF->>SM: getInstance()
    deactivate MPF
    
    User->>MPA: selectPlayStrategy(type)
    MPA->>SM: getCurrentStrategy()
    SM->>MPF: setPlayStrategy()
    
    User->>MPA: loadPlaylist(name)
    MPA->>PM: getPlaylistByName()
    MPA->>MPF: loadPlaylist()
    MPF->>SM: getStrategy()
    
    User->>MPA: connectToAudioDevice(type)
    MPA->>DM: connect()
    DM->>IOD: Create Adaptor
    
    User->>MPA: playSingleSong(name)
    MPA->>MPF: play(song)
    MPF->>AE: play(device, song)
    AE->>IOD: playAudio(song)
    deactivate MPA
```

## Class Hierarchy

### Strategy Hierarchy
```
IPlayStrategy (Interface)
├── SequentialPlayStrategy
├── RandomPlayStrategy
└── CustomQueueStrategy
```

### Device Hierarchy
```
IAudioOutputDevice (Interface)
├── BluetoothSpeakerAdaptor
├── WiredSpeakerAdaptor
└── HeadphonesAdaptor
```

### Manager Hierarchy (All Singleton)
```
Manager (Abstract Concept)
├── DeviceManager
├── PlaylistManager
└── StrategyManager
```

## Dependency Graph

### Core Dependencies
```
MusicPlayerApplication
├── MusicPlayerFacade
│   ├── AudioEngine
│   ├── DeviceManager
│   ├── PlaylistManager
│   └── StrategyManager
└── PlaylistManager
    └── Playlist
        └── Song

DeviceManager
└── IAudioOutputDevice
    ├── BluetoothSpeakerAdaptor
    ├── WiredSpeakerAdaptor
    └── HeadphonesAdaptor

StrategyManager
└── IPlayStrategy
    ├── SequentialPlayStrategy
    ├── RandomPlayStrategy
    └── CustomQueueStrategy
```

## Object Creation Sequence

### Application Startup
```
1. MusicPlayerApplication.getInstance()
2.   ↓ Creates
3. MusicPlayerFacade
4.   ├─ Creates AudioEngine
5.   └─ Gets Manager Singletons
6.       ├─ DeviceManager.getInstance()
7.       ├─ PlaylistManager.getInstance()
8.       └─ StrategyManager.getInstance()
```

### Playback Initialization
```
1. selectPlayStrategy(SEQUENTIAL)
2.   ↓ Delegates to
3. StrategyManager.getCurrentStrategy()
4.   ↓ Creates
5. SequentialPlayStrategy
6.   ↓ Set in
7. MusicPlayerFacade

8. loadPlaylist("Playlist Name")
9.   ↓ Delegates to
10. PlaylistManager.getPlaylistByName()
11.   ↓ Gets
12. Playlist with Songs
13.   ↓ Strategy loads
14. Songs List
```

### Device Connection
```
1. connectToAudioDevice(BLUETOOTH)
2.   ↓ Delegates to
3. DeviceManager.connect()
4.   ↓ Calls
5. DeviceFactory.createDevice()
6.   ↓ Creates
7. BluetoothSpeakerAdaptor
8.   ↓ Wraps
9. BluetoothSpeakerApi
```
