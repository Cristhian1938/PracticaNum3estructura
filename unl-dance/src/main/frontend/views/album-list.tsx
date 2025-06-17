import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { Button, ComboBox, DatePicker, Dialog, Grid, GridColumn, GridItemModel, TextField, VerticalLayout } from '@vaadin/react-components';
import { Notification } from '@vaadin/react-components/Notification';
import { TaskService } from 'Frontend/generated/endpoints';
import { useSignal } from '@vaadin/hilla-react-signals';
import handleError from 'Frontend/views/_ErrorHandler';
import { Group, ViewToolbar } from 'Frontend/components/ViewToolbar';

import { useDataProvider } from '@vaadin/hilla-react-crud';
import Album from 'Frontend/generated/unl/dance/base/models/Album';
import { useCallback, useEffect, useState } from 'react';
import { AlbumService } from 'Frontend/generated/endpoints';

export const config: ViewConfig = {
  title: 'Album',
  menu: {
    icon: 'vaadin:clipboard-check',
    order: 1,
    title: 'Album',
  },
};


type AlbumEntryFormProps = {
  onAlbumCreated?: () => void;
};

type AlbumEntryFormPropsUpdate = ()=> {
  onAlbumUpdated?: () => void;
};
//GUARDAR Album
function AlbumEntryForm(props: AlbumEntryFormProps) {
  const nombre = useSignal('');
  const fecha = useSignal('');
  const createAlbum = async () => {
    try {
      if (nombre.value.trim().length > 0) {
        await AlbumService.createAlbum(nombre.value, fecha.value);
        if (props.onAlbumCreated) {
          props.onAlbumCreated();
        }
        nombre.value = '';
        
        dialogOpened.value = false;
        Notification.show('Album creado', { duration: 5000, position: 'bottom-end', theme: 'success' });
      } else {
        Notification.show('No se pudo crear, faltan datos', { duration: 5000, position: 'top-center', theme: 'error' });
      }

    } catch (error) {
      console.log(error);
      handleError(error);
    }
  };
  
  
  const dialogOpened = useSignal(false);
  return (
    <>
      <Dialog
        modeless
        headerTitle="Nuevo Album"
        opened={dialogOpened.value}
        onOpenedChanged={({ detail }) => {
          dialogOpened.value = detail.value;
        }}
        footer={
          <>
            <Button
              onClick={() => {
                dialogOpened.value = false;
              }}
            >
              Candelar
            </Button>
            <Button onClick={createAlbum} theme="primary">
              Registrar
            </Button>
            
          </>
        }
      >
        <VerticalLayout style={{ alignItems: 'stretch', width: '18rem', maxWidth: '100%' }}>
          <TextField label="Nombre del Album" 
            placeholder="Ingrese el nombre del Album"
            aria-label="Nombre del Album"
            value={nombre.value}
            onValueChanged={(evt) => (nombre.value = evt.detail.value)}
          />
        </VerticalLayout>
          
      </Dialog>
      <Button
            onClick={() => {
              dialogOpened.value = true;
            }}
          >
            Agregar
          </Button>
    </>
  );
}




//LISTA DE Albums
export default function AlbumView() {
  
  const dataProvider = useDataProvider<Album>({
    list: () => AlbumService.listAll(),
  });

  

  function indexIndex({model}:{model:GridItemModel<Album>}) {
    return (
      <span>
        {model.index + 1} 
      </span>
    );
  }

  return (

    <main className="w-full h-full flex flex-col box-border gap-s p-m">

      <ViewToolbar title="Lista de Albums">
        <Group>
          <AlbumEntryForm onAlbumCreated={dataProvider.refresh}/>
        </Group>
      </ViewToolbar>
      <Grid dataProvider={dataProvider.dataProvider}>
        <GridColumn  renderer={indexIndex} header="Nro" />
        <GridColumn path="nombre" header="Nombre del Album" />
        <GridColumn path="" header="">

        </GridColumn>
        
      </Grid>
    </main>
  );
}
