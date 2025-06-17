import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { Button, ComboBox, DatePicker, Dialog, Grid, GridColumn, GridItemModel, TextField, VerticalLayout } from '@vaadin/react-components';
import { Notification } from '@vaadin/react-components/Notification';
import { TaskService } from 'Frontend/generated/endpoints';
import { useSignal } from '@vaadin/hilla-react-signals';
import handleError from 'Frontend/views/_ErrorHandler';
import { Group, ViewToolbar } from 'Frontend/components/ViewToolbar';

import { useDataProvider } from '@vaadin/hilla-react-crud';
import Genero from 'Frontend/generated/unl/dance/base/models/Genero';
import { useCallback, useEffect, useState } from 'react';
import { GeneroService } from 'Frontend/generated/endpoints';

export const config: ViewConfig = {
  title: 'Generos',
  menu: {
    icon: 'vaadin:clipboard-check',
    order: 1,
    title: 'Generos',
  },
};


type GeneroEntryFormProps = {
  onGeneroCreated?: () => void;
};

type GeneroEntryFormPropsUpdate = ()=> {
  onGeneroUpdated?: () => void;
};
//GUARDAR GENERO
function GeneroEntryForm(props: GeneroEntryFormProps) {
  const nombre = useSignal('');
  const createGenero = async () => {
    try {
      if (nombre.value.trim().length > 0) {
        await GeneroService.createGenero(nombre.value);
        if (props.onGeneroCreated) {
          props.onGeneroCreated();
        }
        nombre.value = '';
        
        dialogOpened.value = false;
        Notification.show('Genero creado', { duration: 5000, position: 'bottom-end', theme: 'success' });
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
        headerTitle="Nuevo genero"
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
            <Button onClick={createGenero} theme="primary">
              Registrar
            </Button>
            
          </>
        }
      >
        <VerticalLayout style={{ alignItems: 'stretch', width: '18rem', maxWidth: '100%' }}>
          <TextField label="Nombre del genero" 
            placeholder="Ingrese el nombre del genero"
            aria-label="Nombre del genero"
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




//LISTA DE Generos
export default function GeneroView() {
  
  const dataProvider = useDataProvider<Genero>({
    list: () => GeneroService.listAll(),
  });

  

  function indexIndex({model}:{model:GridItemModel<Genero>}) {
    return (
      <span>
        {model.index + 1} 
      </span>
    );
  }

  return (

    <main className="w-full h-full flex flex-col box-border gap-s p-m">

      <ViewToolbar title="Lista de Generos">
        <Group>
          <GeneroEntryForm onGeneroCreated={dataProvider.refresh}/>
        </Group>
      </ViewToolbar>
      <Grid dataProvider={dataProvider.dataProvider}>
        <GridColumn  renderer={indexIndex} header="Nro" />
        <GridColumn path="nombre" header="Nombre del genero" />
        <GridColumn path="" header="">

        </GridColumn>
        
      </Grid>
    </main>
  );
}
