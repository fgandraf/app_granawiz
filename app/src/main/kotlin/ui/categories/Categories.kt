package ui.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import ui.categories.components.AddButton
import ui.common.AddressView
import ui.categories.components.ListItem
import ui.common.SearchBar

@Composable
fun Categories(){

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {

        //===== HEADER
        Column(modifier = Modifier.fillMaxWidth().padding(15.dp)
        ) {
            Row(modifier = Modifier.height(40.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row { AddressView("icons/system/category.svg","Categorias" ) }

                SearchBar(onTuneClicked = { /* TO DO */ }, onSearchClicked = { /* TO DO */ })
            }
        }


        //===== BODY
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxSize()
                .padding(120.dp, 50.dp, 120.dp, 100.dp)
                .shadow(elevation = 1.dp, shape = RoundedCornerShape(20.dp))
                .border(0.5.dp, MaterialTheme.colors.primaryVariant, shape = RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colors.onPrimary)
        ) {

            Row(modifier = Modifier.padding(30.dp)) {

                //===== FIRST COLUMN
                Row(modifier = Modifier.weight(1.5f).fillMaxHeight()) {
                    Column(modifier = Modifier.padding(top = 35.dp)) {
                        ListItem(iconResource = "icons/system/gastos.svg", label = "Gastos", separator = true, trailingIcon = "icons/system/toggle_right.svg") { }
                        ListItem(iconResource = "icons/system/rendimentos.svg", label = "Rendimentos", separator = true, trailingIcon = "icons/system/toggle_right.svg") { }
                    }
                }
                Divider(modifier = Modifier.width(2.dp).fillMaxHeight())


                //===== SECOND COLUMN
                Row(modifier = Modifier.weight(3f).fillMaxHeight()) {
                    Column(verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column(modifier = Modifier.fillMaxWidth().padding(top = 35.dp)) {
                            ListItem(iconResource = "icons/pack/food.svg", label = "Alimentação", separator = true, trailingIcon = "icons/system/toggle_right.svg") { }
                            ListItem(iconResource = "icons/pack/shop.svg", label = "Compras", separator = true, trailingIcon = "icons/system/toggle_right.svg") { }
                            ListItem(iconResource = "icons/pack/education.svg", label = "Educação", separator = true, trailingIcon = "icons/system/toggle_right.svg") { }
                            ListItem(iconResource = "icons/pack/fun.svg", label = "Lazer", separator = true, trailingIcon = "icons/system/trash.svg") { }
                            ListItem(iconResource = "icons/pack/house.svg", label = "Moradia", separator = true, trailingIcon = "icons/system/toggle_right.svg") { }
                            ListItem(iconResource = "icons/pack/truck.svg", label = "Operacionais", separator = true, trailingIcon = "icons/system/trash.svg") { }
                            ListItem(iconResource = "icons/pack/question.svg", label = "Outros", separator = true, trailingIcon = "icons/system/trash.svg") { }
                            ListItem(iconResource = "icons/pack/medicine.svg", label = "Saúde", separator = true, trailingIcon = "icons/system/toggle_right.svg") { }
                            ListItem(iconResource = "icons/pack/weight.svg", label = "Taxas e Impostos", separator = true, trailingIcon = "icons/system/trash.svg") { }
                            ListItem(iconResource = "icons/pack/car.svg", label = "Transporte", separator = true, trailingIcon = "icons/system/trash.svg") { }
                            ListItem(iconResource = "icons/pack/clothes.svg", label = "Vestimentas", separator = true, trailingIcon = "icons/system/toggle_right.svg") { }
                        }
                        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                            Row(modifier = Modifier.width(130.dp), horizontalArrangement = Arrangement.Center) {
                                AddButton { }
                            }
                        }
                    }
                }
                Divider(modifier = Modifier.width(2.dp).fillMaxHeight())


                //===== THIRD COLUMN
                Row(modifier = Modifier.weight(3f).fillMaxHeight()) {
                    Column(verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column(modifier = Modifier.fillMaxWidth().padding(top = 35.dp)) {
                            ListItem(label = "Conveniência", separator = true, trailingIcon = "icons/system/trash.svg") { }
                            ListItem(label = "Lanchonete", separator = true, trailingIcon = "icons/system/trash.svg") { }
                            ListItem(label = "Restaurante", separator = true, trailingIcon = "icons/system/trash.svg") { }
                            ListItem(label = "Outros", separator = true, trailingIcon = "icons/system/trash.svg") { }
                        }
                        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                            Row(modifier = Modifier.width(130.dp), horizontalArrangement = Arrangement.Center) {
                                AddButton { }
                            }
                        }
                    }
                }
                Divider(modifier = Modifier.width(2.dp).fillMaxHeight())
            }
        }
    }
}