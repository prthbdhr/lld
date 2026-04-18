# Sequence Diagrams - Music Player System

## 1. Playing a Single Song Sequence

```mermaid
sequenceDiagram
    actor User
    participant App as MusicPlayerApp
    participant Facade as MusicPlayerFacade
    participant Engine as AudioEngine
    participant DM as DeviceManager
    participant Output as IAudioOutputDevice
    
    User->>App: playSingleSong("Kesariya")
    activate App
    
    App->>App: getSongByTitle("Kesariya")
    activate App
    App-->>App: Returns Song object
    deactivate App
    
    App->>Facade: play(song)
    activate Facade
    
    Facade->>DM: hasConnectedDevice()
    activate DM
    DM-->>Facade: true
    deactivate DM
    
    Facade->>DM: getConnectedOutputDevice()
    activate DM
    DM-->>Facade: IAudioOutputDevice
    deactivate DM
    
    Facade->>Engine: play(device, song)
    activate Engine
    
    note over Engine: Check if resuming same song
    alt Same song is paused
        Engine->>Engine: isPaused && currentSong == song
        activate Engine
        Engine->>Output: playAudio(song)
        activate Output
        Output-->>Engine: Playback resumed
        deactivate Output
        Engine-->>Facade: Audio playing (resumed)
        deactivate Engine
    else Different song
        Engine->>Engine: currentSong != song
        activate Engine
        alt Current song was playing
            Engine->>Engine: Stop current song
        end
        Engine->>Engine: Load new song
        Engine->>Output: playAudio(song)
        activate Output
        Output-->>Engine: Audio playing
        deactivate Output
        Engine-->>Facade: Audio playing (new)
        deactivate Engine
    end
    
    deactivate Engine
    Facade-->>App: Operation complete
    deactivate Facade
    
    App-->>User: Song is now playing
    deactivate App
```

## 2. Loading Playlist with Strategy Sequence

```mermaid
sequenceDiagram
    actor User
    participant App as MusicPlayerApp
    participant Facade as MusicPlayerFacade
    participant StratMgr as StrategyManager
    participant PlayMgr as PlaylistManager
    participant Strategy as IPlayStrategy
    
    User->>App: selectPlayStrategy(SEQUENTIAL)
    activate App
    
    App->>Facade: setPlayStrategy(PlayStrategyType)
    activate Facade
    
    Facade->>StratMgr: getCurrentStrategy(SEQUENTIAL)
    activate StratMgr
    
    StratMgr->>StratMgr: Check strategy type
    activate StratMgr
    StratMgr->>Strategy: new SequentialPlayStrategy()
    activate Strategy
    Strategy-->>StratMgr: strategy instance
    deactivate Strategy
    deactivate StratMgr
    
    StratMgr-->>Facade: strategy instance
    deactivate StratMgr
    
    Facade->>Facade: playStrategy = strategy
    deactivate Facade
    
    App-->>User: Strategy selected
    deactivate App
    
    User->>App: loadPlaylist("Bollywood Vibes")
    activate App
    
    App->>PlayMgr: getPlaylistByName("Bollywood Vibes")
    activate PlayMgr
    PlayMgr-->>App: Playlist object
    deactivate PlayMgr
    
    App->>Facade: loadPlaylist(name)
    activate Facade
    
    Facade->>PlayMgr: getPlaylistByName(name)
    activate PlayMgr
    PlayMgr-->>Facade: Playlist
    deactivate PlayMgr
    
    Facade->>Strategy: setPlaylist(playlist)
    activate Strategy
    
    Strategy->>Strategy: Initialize currentIndex = 0
    Strategy->>Strategy: Store playlist reference
    Strategy->>Strategy: Parse songs list
    
    Strategy-->>Facade: Playlist loaded
    deactivate Strategy
    
    Facade->>Facade: loadedPlaylist = playlist
    Facade-->>App: Playlist loaded and ready
    deactivate Facade
    
    App-->>User: Ready to play
    deactivate App
```

## 3. Device Connection Sequence

```mermaid
sequenceDiagram
    actor User
    participant App as MusicPlayerApp
    participant DM as DeviceManager
    participant Factory as DeviceFactory
    participant Device as IAudioOutputDevice
    participant External as ExternalAPI
    
    User->>App: connectToAudioDevice(BLUETOOTH)
    activate App
    
    App->>DM: connect(BLUETOOTH)
    activate DM
    
    note over DM: Disconnect current device if any
    DM->>DM: Check currentOutputDevice
    alt Device already connected
        DM->>DM: Set currentOutputDevice = null
    end
    
    DM->>Factory: createDevice(BLUETOOTH)
    activate Factory
    
    Factory->>Factory: Switch on deviceType
    activate Factory
    
    Factory->>Device: new BluetoothSpeakerAdaptor()
    activate Device
    
    Device->>External: Create BluetoothSpeakerApi
    activate External
    External-->>Device: api instance
    deactivate External
    
    Device-->>Factory: adaptor instance
    deactivate Device
    deactivate Factory
    
    Factory-->>DM: IAudioOutputDevice
    deactivate Factory
    
    DM->>DM: currentOutputDevice = device
    
    DM->>DM: Print connection status
    activate DM
    DM->>DM: "Connected to Bluetooth Speaker..."
    deactivate DM
    
    DM-->>App: Connection complete
    deactivate DM
    
    App-->>User: Device connected
    deactivate App
```

## 4. Playing All Tracks from Playlist Sequence

```mermaid
sequenceDiagram
    actor User
    participant App as MusicPlayerApp
    participant Facade as MusicPlayerFacade
    participant Engine as AudioEngine
    participant Strategy as IPlayStrategy
    participant Device as IAudioOutputDevice
    
    User->>App: playAllTracksInPlaylist()
    activate App
    
    App->>Facade: playAllTracks()
    activate Facade
    
    note over Facade: Check if playlist is loaded
    Facade->>Facade: loadedPlaylist != null
    activate Facade
    
    alt Playlist not loaded
        Facade-->>User: RuntimeException: No playlist loaded
    else Playlist loaded
        deactivate Facade
        
        Facade->>Strategy: hasNext()
        activate Strategy
        Strategy-->>Facade: true for first song
        deactivate Strategy
        
        loop Play all tracks
            Facade->>Strategy: next()
            activate Strategy
            Strategy-->>Facade: Next Song
            deactivate Strategy
            
            Facade->>Engine: play(device, song)
            activate Engine
            
            Engine->>Device: playAudio(song)
            activate Device
            Device-->>Engine: Audio playing
            deactivate Device
            
            Engine-->>Facade: Song playing
            deactivate Engine
            
            note over Facade: Wait for user input or<br/>automatic next
            
            Facade->>Strategy: hasNext()
            activate Strategy
            
            note over Strategy: Check if more songs exist
            alt More songs available
                Strategy-->>Facade: true
            else No more songs
                Strategy-->>Facade: false
            end
            deactivate Strategy
        end
        
        Facade-->>App: Playlist finished
        deactivate Facade
    end
    
    App-->>User: Playback complete
    deactivate App
```

## 5. Pause and Resume Sequence

```mermaid
sequenceDiagram
    actor User
    participant App as MusicPlayerApp
    participant Facade as MusicPlayerFacade
    participant Engine as AudioEngine
    participant Device as IAudioOutputDevice
    
    note over User,Device: Currently playing "Kesariya"
    
    User->>App: pauseCurrentSong("Kesariya")
    activate App
    
    App->>App: getSongByTitle("Kesariya")
    activate App
    App-->>App: Returns Song object
    deactivate App
    
    App->>Facade: pause(song)
    activate Facade
    
    Facade->>Engine: pause()
    activate Engine
    
    Engine->>Engine: Verify song being played
    activate Engine
    Engine->>Engine: currentSongTitle.equals(song.getTitle())
    deactivate Engine
    
    alt Song match
        Engine->>Engine: isPaused = true
        Engine->>Engine: Print "Pausing [song title]"
        Engine-->>Facade: Paused successfully
    else Song mismatch
        Engine-->>Facade: RuntimeException: Cannot pause
    end
    
    deactivate Engine
    Facade-->>App: Song paused
    deactivate Facade
    
    App-->>User: Song is paused
    deactivate App
    
    note over User,Device: User wants to resume
    
    User->>App: playSingleSong("Kesariya")
    activate App
    
    App->>App: getSongByTitle("Kesariya")
    activate App
    App-->>App: Returns Song object
    deactivate App
    
    App->>Facade: play(song)
    activate Facade
    
    Facade->>Engine: play(device, song)
    activate Engine
    
    Engine->>Engine: Check pause state
    activate Engine
    Engine->>Engine: isPaused && currentSong == song
    deactivate Engine
    
    alt Paused and same song
        Engine->>Engine: isPaused = false
        Engine->>Engine: Print "Resuming [song title]"
        Engine->>Device: playAudio(song)
        activate Device
        Device-->>Engine: Playback resumed
        deactivate Device
        Engine-->>Facade: Playback resumed
    end
    
    deactivate Engine
    Facade-->>App: Song playing
    deactivate Facade
    
    App-->>User: Song resumed
    deactivate App
```

## 6. Custom Queue Strategy Sequence

```mermaid
sequenceDiagram
    actor User
    participant App as MusicPlayerApp
    participant Facade as MusicPlayerFacade
    participant StratMgr as StrategyManager
    participant Strategy as CustomQueueStrategy
    
    User->>App: selectPlayStrategy(CUSTOM_QUEUE)
    activate App
    
    App->>Facade: setPlayStrategy(PlayStrategyType.CUSTOM_QUEUE)
    activate Facade
    
    Facade->>StratMgr: getCurrentStrategy(CUSTOM_QUEUE)
    activate StratMgr
    
    StratMgr->>Strategy: new CustomQueueStrategy()
    activate Strategy
    Strategy->>Strategy: customQueue = new Queue()
    Strategy-->>StratMgr: strategy instance
    deactivate Strategy
    
    StratMgr-->>Facade: strategy instance
    deactivate StratMgr
    
    Facade->>Facade: playStrategy = strategy
    deactivate Facade
    
    App-->>User: Strategy selected
    deactivate App
    
    User->>App: loadPlaylist("Bollywood Vibes")
    activate App
    App->>Facade: loadPlaylist(name)
    activate Facade
    Facade->>Strategy: setPlaylist(playlist)
    activate Strategy
    Strategy-->>Facade: Loaded
    deactivate Strategy
    Facade-->>App: Loaded
    deactivate Facade
    App-->>User: Playlist loaded
    deactivate App
    
    User->>App: QueuesNextSong("Kesariya")
    activate App
    
    App->>App: getSongByTitle("Kesariya")
    activate App
    App-->>App: Song object
    deactivate App
    
    App->>Facade: addToQueue(song)
    activate Facade
    
    Facade->>Strategy: addToNext(song)
    activate Strategy
    
    Strategy->>Strategy: customQueue.add(song)
    Strategy->>Strategy: Print "Added to queue: Kesariya"
    
    Strategy-->>Facade: Added
    deactivate Strategy
    
    Facade-->>App: Added to queue
    deactivate Facade
    
    App-->>User: Song queued
    deactivate App
    
    User->>App: QueuesNextSong("Chaiyya Chaiyya")
    activate App
    App->>App: getSongByTitle("Chaiyya Chaiyya")
    activate App
    App-->>App: Song object
    deactivate App
    App->>Facade: addToQueue(song)
    activate Facade
    Facade->>Strategy: addToNext(song)
    activate Strategy
    Strategy->>Strategy: customQueue.add(song)
    Strategy-->>Facade: Added
    deactivate Strategy
    Facade-->>App: Added to queue
    deactivate Facade
    App-->>User: Song queued
    deactivate App
    
    note over User,Strategy: Custom queue ready: [Kesariya, Chaiyya Chaiyya]
    
    User->>App: playAllTracksInPlaylist()
    activate App
    App->>Facade: playAllTracks()
    activate Facade
    
    loop Play queued songs in order
        Facade->>Strategy: hasNext()
        activate Strategy
        Strategy->>Strategy: !customQueue.isEmpty()
        Strategy-->>Facade: true
        deactivate Strategy
        
        Facade->>Strategy: next()
        activate Strategy
        Strategy->>Strategy: customQueue.poll()
        Strategy-->>Facade: Song from queue
        deactivate Strategy
        
        note over Facade: Play the song via AudioEngine
        Facade->>Facade: Play song
    end
    
    Facade-->>App: Queue played
    deactivate Facade
    
    App-->>User: Playback complete
    deactivate App
```

## 7. Complete Application Initialization Sequence

```mermaid
sequenceDiagram
    participant Main
    participant App as MusicPlayerApp
    participant Facade as MusicPlayerFacade
    participant DevMgr as DeviceManager
    participant PlayMgr as PlaylistManager
    participant StratMgr as StrategyManager
    
    Main->>App: getInstance()
    activate App
    
    note over App: Check if instance exists
    App->>App: instance == null
    activate App
    
    alt First call
        App->>App: synchronized block
        activate App
        
        App->>App: new MusicPlayerApplication()
        activate App
        App->>App: songLibrary = new ArrayList()
        App-->>App: Instance created
        deactivate App
        
        deactivate App
    else Already exists
        note over App: Return existing instance
    end
    deactivate App
    
    App-->>Main: MusicPlayerApplication
    deactivate App
    
    Main->>Facade: getInstance()
    activate Facade
    
    note over Facade: Similar singleton pattern
    Facade->>Facade: instance == null
    alt First call
        Facade->>Facade: new MusicPlayerFacade()
        activate Facade
        
        Facade->>Facade: audioEngine = new AudioEngine()
        Facade->>Facade: loadedPlaylist = null
        Facade->>Facade: playStrategy = null
        
        Facade->>DevMgr: getInstance()
        activate DevMgr
        DevMgr->>DevMgr: Create DeviceManager
        DevMgr-->>Facade: DeviceManager
        deactivate DevMgr
        
        Facade->>PlayMgr: getInstance()
        activate PlayMgr
        PlayMgr->>PlayMgr: Create PlaylistManager
        PlayMgr-->>Facade: PlaylistManager
        deactivate PlayMgr
        
        Facade->>StratMgr: getInstance()
        activate StratMgr
        StratMgr->>StratMgr: Create StrategyManager
        StratMgr-->>Facade: StrategyManager
        deactivate StratMgr
        
        Facade-->>Facade: Instance created
        deactivate Facade
    end
    
    Facade-->>Main: MusicPlayerFacade
    deactivate Facade
    
    Main->>Main: Application ready for use
```

---

## Summary of Key Interactions

### 1. Playback Flow
1. User selects strategy
2. Facade gets strategy from StrategyManager
3. User loads playlist
4. Strategy initializes with playlist songs
5. User initiates playback
6. AudioEngine communicates with device
7. Output device plays the audio

### 2. Device Management Flow
1. User requests device connection
2. DeviceManager uses Factory to create adapter
3. Adapter wraps external API
4. DeviceManager stores reference
5. AudioEngine uses device for playback

### 3. Strategy Selection Flow
1. User selects strategy type
2. StrategyManager creates appropriate strategy
3. Facade stores strategy
4. Strategy is ready for playlist operations

### 4. Error Handling Patterns
- Check device connection before playback
- Verify playlist is loaded
- Validate song exists in library
- Confirm pause target matches current song
- Handle invalid strategy types

---

## Important Notes

1. **Thread Safety**: All Singletons use synchronized blocks in getInstance()
2. **Resource Management**: Device switching properly sets old device reference to null
3. **Strategy Pattern**: Strategies can be swapped at runtime
4. **Adapter Pattern**: External APIs are wrapped, not directly used
5. **Facade Pattern**: All complexity hidden behind MusicPlayerFacade
